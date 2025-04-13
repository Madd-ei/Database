function sendReq() {
    return fetch ("http://localhost:8080/hello", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Network reponse not ok");
        }
        return response.text();
    });
}