import React from "react";
import axios from "../http";
import auth from "../auth/Auth";
import { useEffect, useState } from "react";

export default function Admin(props) {

    const [user, setUser] = useState(null);

    const logout = (event) => {
        event.preventDefault();
        localStorage.removeItem("accessToken");
        auth.logout(() => {
            props.history.push("/")
        });
    }

    const createUser = (event) => {
        event.preventDefault();
        const userName = document.getElementById("Username").value;
        const password = document.getElementById("Password").value;
        const accBalance = parseInt(document.getElementById("AccBalance").value);
        const role = document.getElementById("Role").value;
        console.log(role)
        axios.post("/createUser", {
            "userName": userName,
            "password": password,
            "accBalance": accBalance,
            "role": role
        }, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem("accessToken")
            }
        }).then((response) => {
            alert(response.data.response)
        }).catch((error) => {
            alert("User Already Exists!");
        })
    }

    useEffect(() => {
        axios.get("/getUser", {
            headers: {
                Authorization: "Bearer " + localStorage.getItem("accessToken")
            }
        }).then((response) => {
            setUser(response.data.user);
        }).catch((error) => {
            console.error(error);
        })
    });

    return (
        <div className="Admin">
            <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">Welcome {user}!</a>
                    <button onClick={logout} className="btn btn-primary">Logout</button>
                </div>
            </nav>
            <div className="CardBody">
                <div className="container CardBody">
                    <div className="col admin">
                        <form onSubmit={createUser} id="form">
                            <div className="mb-3">
                                <label htmlFor="Username" className="form-label">Username</label>
                                <input type="text" className="form-control" id="Username" aria-describedby="emailHelp" />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="Password" className="form-label">Password</label>
                                <input type="password" className="form-control" id="Password" />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="Role" className="form-label">Account Balance</label>
                                <input type="text" className="form-control" id="AccBalance" />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="AccBalance" className="form-label">Role</label>
                                <input type="password" className="form-control" id="Role" />
                            </div>
                            <button type="submit" className="btn btn-primary">Create User</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}