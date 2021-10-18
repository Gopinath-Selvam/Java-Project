import React from "react";
import { Route, Redirect } from "react-router";
import auth from "../auth/Auth";

export const ProtectedRoute = ({ component: Component, ...rest }) => {
    return (
        <Route
            {...rest}
            render={(props) => {
                if (auth.isAuthenticated() || localStorage.getItem("accessToken") != null) {
                    return <Component {...props}/>
                } else {
                    return <Redirect 
                    to={{
                        pathname: "/",
                        state: {
                            from: props.location
                        }
                    }}
                    />
                }
            }}
        />
    );
}