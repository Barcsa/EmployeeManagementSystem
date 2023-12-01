import React, { useEffect, useState } from 'react';
import { deleteEmployee, listEmployees } from '../services/EmployeeService';
import { useNavigate } from 'react-router-dom';
import { getDepartmentById } from '../services/DepartmentService';
import * as XLSX from 'xlsx';
import Pagination from 'react-bootstrap/Pagination';
import 'bootstrap/dist/css/bootstrap.min.css';

const ListEmployeeComponent = () => {
  const [employees, setEmployees] = useState([]);
  const [sortCriteria, setSortCriteria] = useState('firstName');
  const [sortOrder, setSortOrder] = useState('asc');
  const [search, setSearch] = useState('');
  const [searchAttribute, setSearchAttribute] = useState('department.departmentName');
  const [currentPage, setCurrentPage] = useState(1);
  const [employeesPerPage] = useState(10);
  const navigator = useNavigate();

  useEffect(() => {
    getAllEmployees();
  }, [sortCriteria, sortOrder, currentPage]);

  async function getAllEmployees() {
    try {
      const employeesResponse = await listEmployees();
      const employeesWithDepartments = await Promise.all(
        employeesResponse.data.map(async (employee) => {
          try {
            const departmentResponse = await getDepartmentById(employee.departmentId);
            const department = departmentResponse.data;
            return { ...employee, department };
          } catch (error) {
            console.error("Error fetching department:", error);
            return { ...employee, department: { departmentName: 'Unknown Department' } };
          }
        })
      );

      const sortedEmployees = employeesWithDepartments.sort((a, b) => {
        const valueA = getNestedValue(a, sortCriteria);
        const valueB = getNestedValue(b, sortCriteria);

        if (valueA < valueB) {
          return sortOrder === 'asc' ? -1 : 1;
        }
        if (valueA > valueB) {
          return sortOrder === 'asc' ? 1 : -1;
        }
        return 0;
      });

      setEmployees(sortedEmployees);
    } catch (error) {
      console.error("Error fetching employees:", error);
    }
  }

  function addNewEmployee() {
    navigator('/add-employee');
  }

  function updateEmployee(id) {
    navigator(`/edit-employee/${id}`);
  }

  function removeEmployee(id) {
    deleteEmployee(id)
      .then(() => {
        getAllEmployees();
      })
      .catch((error) => {
        console.error(error);
      });
  }

  async function exportToExcel() {
    await getAllEmployees();

    const dataForExport = employees.map((employee) => ({
      FirstName: employee.firstName,
      LastName: employee.lastName,
      Email: employee.email,
      Department: employee.department.departmentName,
    }));

    const ws = XLSX.utils.json_to_sheet(dataForExport);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Employees');
    XLSX.writeFile(wb, 'employees.xlsx');
  }

  function handleSort(column) {
    if (column !== 'Actions') {
      if (sortCriteria === column) {
        setSortOrder((prevOrder) => (prevOrder === 'asc' ? 'desc' : 'asc'));
      } else {
        setSortCriteria(column);
        setSortOrder('asc');
      }
    }
  }

  function handleSearchAttributeChange(event) {
    setSearchAttribute(event.target.value);
  }

  function getNestedValue(obj, path) {
    const properties = path.split('.');
    return properties.reduce((value, prop) => (value && value[prop] !== undefined ? value[prop] : ''), obj);
  }

  const filteredEmployees = employees.filter((employee) => {
    const searchTerm = getNestedValue(employee, searchAttribute).toLowerCase();
    return searchTerm.includes(search.toLowerCase());
  });

  // Pagination
  const indexOfLastEmployee = currentPage * employeesPerPage;
  const indexOfFirstEmployee = indexOfLastEmployee - employeesPerPage;
  const currentEmployees = filteredEmployees.slice(indexOfFirstEmployee, indexOfLastEmployee);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <div className='container'>
      <h1 className='text-center mb-4' style={{ marginTop: "20px" }}>Employees</h1>
      <div className="d-flex justify-content-between align-items-center mb-4 p-3 rounded">
        <div>
          <button
            className="btn btn-primary mr-2"
            onClick={addNewEmployee}
          >
            Add Employee
          </button>
        </div>
        <div className="d-flex align-items-center">
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Searching by..."
            className="mr-2 form-control"
          />
          <select
            value={searchAttribute}
            onChange={handleSearchAttributeChange}
            className="mr-2 form-select"
            style={{ marginLeft: "10px" }}
          >
            <option value="firstName">First Name</option>
            <option value="lastName">Last Name</option>
            <option value="email">Email</option>
            <option value="department.departmentName">Department</option>
          </select>
          <button
            className="btn btn-outline-light"
            style={{ marginLeft: "10px" }}
            onClick={() => setSearch('')}
          >
            Clear
          </button>
        </div>
        <div>
          <button
            className="btn btn-success"
            onClick={exportToExcel}
          >
            Export to Excel
          </button>
        </div>
      </div>

      <table className='table table-striped table-bordered table-dark'>
        <thead>
          <tr>
            <th style={{ color: "red" }}>#</th>
            <th onClick={() => handleSort('firstName')} style={{ userSelect: 'none', color: "red" }}>
              Employee First Name
            </th>
            <th onClick={() => handleSort('lastName')} style={{ userSelect: 'none', color: "red"  }}>
              Employee Last Name
            </th>
            <th onClick={() => handleSort('email')} style={{ userSelect: 'none', color: "red"  }}>
              Employee Email
            </th>
            <th onClick={() => handleSort('department.departmentName')} style={{ userSelect: 'none', color: "red"  }}>
              Employee Department
            </th>
            <th style={{ color: "red" }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {currentEmployees.map((employee, index) => (
            <tr key={employee.id}>
              <td>{indexOfFirstEmployee + index + 1}</td>
              <td>{employee.firstName}</td>
              <td>{employee.lastName}</td>
              <td>{employee.email}</td>
              <td>{employee.department ? employee.department.departmentName : 'Unknown Department'}</td>
              <td>
                <button className='btn btn-info' onClick={() => updateEmployee(employee.id)}>
                  Update
                </button>
                <button
                  className='btn btn-danger ' style={{ marginLeft: "10px" }}
                  onClick={() => removeEmployee(employee.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Pagination */}
      <Pagination>
        {Array.from({ length: Math.ceil(filteredEmployees.length / employeesPerPage) }).map((_, index) => (
          <Pagination.Item
            key={index + 1}
            active={index + 1 === currentPage}
            onClick={() => paginate(index + 1)}
          >
            {index + 1}
          </Pagination.Item>
        ))}
      </Pagination>
    </div>
  );
};

export default ListEmployeeComponent;
