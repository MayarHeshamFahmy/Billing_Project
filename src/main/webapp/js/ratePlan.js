// Rate Plan Management JavaScript

if (document.getElementById('createRatePlanForm')) { // Updated form id
    document.getElementById('createRatePlanForm').addEventListener('submit', async (e) => { // Updated form id
        e.preventDefault();
        
        const formData = {
            name: document.getElementById('name').value,
            description: document.getElementById('description').value,
            basePrice: parseFloat(document.getElementById('basePrice').value)
        };

        try {
            const response = await fetch('/BillingSystem/api/rate-plans', { // Updated endpoint
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                alert('Rate Plan created successfully!'); // Updated message
                window.location.href = 'index.html';
            } else {
                const error = await response.json();
                alert('Error: ' + error.message);
            }
        } catch (error) {
            console.error('Error creating rate plan:', error); // Updated message
            alert('Error creating rate plan. Please try again.'); // Updated message
        }
    });
} 