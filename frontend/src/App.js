import './App.css';
import {useEffect, useState} from "react";

const signIn = () => {
    const baseUrl = process.env.BASE_URL || ""
    window.location = `http://localhost:8080/login`
}

function App() {

    const [message, setMessage] = useState(null);

    useEffect(() => {
        fetch("/api/message")
            .then(res => {
                if(res.status === 401 || res.status === 403) {
                    signIn()
                } else {
                    return res.json()
                }
            })
            .then(
                result => {
                    setMessage(result.message);
                },
                reject => {
                    console.dir(reject)
                })
    }, [])

    return (
        <div className="App">
            {message}
        </div>
    );
}

export default App;
