console.log("connect.js is loaded!");
let root = null;

function init() {
    const container = document.getElementById("java");
    root = ReactDOM.createRoot(container);

    loadResponse();

    const insertBtn = document.getElementById("insert-btn");
    insertBtn.addEventListener("click", () => {
        rend("insert");
    });
}

function sendReq() {
    console.log("Sending fetch request...");
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

async function loadResponse() {
    console.log("loadResponse called")
    const res = await sendReq();
    root.render(res);
}

function insert() {
    const insert = <form><label for='insert'>Please provide which table you would like to insert into: </label><input id='insert'></input></form>;
    return insert;
}

function rend(fun) {
    if (fun == "insert") {
        root.render(insert());
    }
}

init();