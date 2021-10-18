import axios from "../http";
import React, { useEffect, useState } from "react";
import auth from "../auth/Auth";
import "../styles/Styles.css"

export default function User(props) {

    const [user, setUser] = useState(null);
    const [accBalance, setAccBalance] = useState(0);

    const logout = (event) => {
        event.preventDefault();
        localStorage.removeItem("accessToken");
        auth.logout(() => {
            props.history.push("/")
        });
    }

    const credit = () => {
        axios.post("/credit", {
            "accBalance": document.getElementById("Amount").value
        }, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem("accessToken")
            }
        }).then(() => {
            axios.get("/getUser", {
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("accessToken")
                }
            }).then((response) => {
                setUser(response.data.user);
                setAccBalance(response.data.accBalance);
            }).catch((error) => {
                console.error(error);
            })
            document.getElementById("Amount").value = ""
        }).catch((error) => {
            console.error(error)
        })
    }

    const debit = () => {
        axios.post("/debit", {
            "accBalance": document.getElementById("Amount").value
        }, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem("accessToken")
            }
        }).then(() => {
            axios.get("/getUser", {
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("accessToken")
                }
            }).then((response) => {
                setUser(response.data.user);
                setAccBalance(response.data.accBalance);
            }).catch((error) => {
                console.error(error);
            })
            document.getElementById("Amount").value = ""
        }).catch((error) => {
            console.error(error)
        })
    }

    useEffect(() => {
        axios.get("/getUser", {
            headers: {
                Authorization: "Bearer " + localStorage.getItem("accessToken")
            }
        }).then((response) => {
            setUser(response.data.user);
            setAccBalance(response.data.accBalance);
        }).catch((error) => {
            console.error(error);
        })
    });

    return (
        <div className="User">
            <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">Welcome {user}!</a>
                    <button onClick={logout} className="btn btn-primary">Logout</button>
                </div>
            </nav>
            <div className="CardBody">
                <div className="container CardBody">
                    <div className="row row-cols-2">
                        <div className="col">
                            <div className="card user">
                                <div className="card-body">
                                    Your Current Account Balance is {accBalance}
                                </div>
                            </div>
                        </div>
                        <div className="col">
                            <div className="card user">
                                <div className="card-body">
                                    <div className="mb-3 form">
                                        <label htmlFor="Amount" className="form-label">Enter Ammount</label>
                                        <input type="text" className="form-control" id="Amount" />
                                    </div>
                                    <button onClick={credit} className="btn btn-primary">Credit</button>
                                    <button onClick={debit} className="btn btn-primary">Debit</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}