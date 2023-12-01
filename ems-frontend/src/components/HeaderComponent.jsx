import React from "react";
import { NavLink } from "react-router-dom";

const HeaderComponent = () => {
  return (
    <div>
      <header>
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
          <a className="navbar-brand ms-2" href="http://localhost:3000/" style={{fontWeight: "bold"}}>
            Employee Management System
          </a>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav">
              <li className="nav-item">
                <NavLink className= 'nav-link' to='/employees' style={{color : "aqua", fontWeight: "bold"}}>Employees</NavLink>
              </li>
              <li className="nav-item">
                <NavLink className= 'nav-link' to='/departments' style={{color: "green", fontWeight: "bold"}}>Departments</NavLink>
              </li>
            </ul>
          </div>
        </nav>
      </header>
    </div>
  );
};

export default HeaderComponent;
