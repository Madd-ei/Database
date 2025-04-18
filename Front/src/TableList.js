import React, { useEffect, useState } from 'react';

function TableList() {
  const [tables, setTables] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8080/tables')
      .then(res => res.json())
      .then(data => {
        setTables(data);
      })
      .catch(err => console.error('Fetch error:', err));
  }, []);

  return (
    <div>
      <h2>Tables</h2>
      <ul>
        {tables.map((table, index) => (
          <li key={index}>{table}</li>
        ))}
      </ul>
    </div>
  );
}

export default TableList;