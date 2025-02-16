document.getElementById('flightForm').addEventListener('submit', function (e) {
    e.preventDefault();
    console.log("Form submitted!");

    const origin = document.getElementById('origin').value;
    const destination = document.getElementById('destination').value;
    const date = document.getElementById('date').value;

    console.log("Fetching flights with:", { origin, destination, date });

    fetch(`http://localhost:8080/api/flights?origin=${origin}&destination=${destination}&date=${date}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log("Data received:", data);
            const resultsDiv = document.getElementById('flightResults');
            resultsDiv.innerHTML = '';
            if (data.length === 0) {
                resultsDiv.innerHTML = '<p>No flights found.</p>';
            } else {
                data.forEach(flight => {
                    resultsDiv.innerHTML += `
                        <div class="flight">
                            <p>Flight Code: ${flight.flightCode}</p>
                            <p>Airline: ${flight.airline}</p>
                            <p>Departure: ${flight.departureTime}</p>
                            <p>Arrival: ${flight.arrivalTime}</p>
                        </div>
                    `;
                });
            }
        })
        .catch(error => {
            console.error('Error fetching flights:', error);
            const resultsDiv = document.getElementById('flightResults');
            resultsDiv.innerHTML = '<p>Error fetching flight data. Please try again later.</p>';
        });
});