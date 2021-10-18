import axios from "../http";
import React from "react";
import '../styles/Styles.css'
import auth from "../auth/Auth";

export default function Login(props) {

    const login = (event) => {
        event.preventDefault();
        const form = document.getElementById("login");
        const params = new URLSearchParams();
        params.append("userName", form.Username.value);
        params.append("password", form.Password.value);
        axios.post("/login", params).then((response) => {
            localStorage.setItem("accessToken", response.data.accessToken);
            auth.login(() => {
                axios.get("/getRole", {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("accessToken")
                    }
                }).then((response) => {
                    if (response.data.role === "user")
                        props.history.push("/user");
                    else
                        props.history.push("/admin");
                })
            });
        }).catch((error) => {
            console.error(error);
        });
    }

    return (
        <div className="Login">
            <h1>User Login</h1>
            <form onSubmit={login} id="login">
                <div className="mb-3">
                    <label htmlFor="Username" className="form-label">Username</label>
                    <input type="text" className="form-control" id="Username" aria-describedby="emailHelp" />
                    <div id="emailHelp" className="form-text">We'll never share your email with anyone else.</div>
                </div>
                <div className="mb-3">
                    <label htmlFor="Password" className="form-label">Password</label>
                    <input type="password" className="form-control" id="Password" />
                </div>
                <button type="submit" className="btn btn-primary">Login</button>
            </form>
        </div>
    );
}