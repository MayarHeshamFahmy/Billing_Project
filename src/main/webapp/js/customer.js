// Customer Management JavaScript

// Load rate plans for the dropdown
async function loadRatePlans() {
    try {
        const response = await fetch('/BillingSystem/api/rate-plans');
        const data = await response.json();
        const ratePlans = data.ratePlans;
        const ratePlanSelect = document.getElementById('profile');
        
        // Clear existing options except the first one
        while (ratePlanSelect.options.length > 1) {
            ratePlanSelect.remove(1);
        }
        
        ratePlans.forEach(ratePlan => {
            const option = document.createElement('option');
            option.value = ratePlan.id;
            option.textContent = `${ratePlan.name} - $${ratePlan.basePrice}`;
            ratePlanSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading rate plans:', error);
    }
}

// Add Customer Form Handler
if (document.getElementById('addCustomerForm')) {
    document.getElementById('addCustomerForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const formData = {
            name: document.getElementById('name').value,
            phoneNumber: document.getElementById('phoneNumber').value,
            email: document.getElementById('email').value,
            address: document.getElementById('address').value,
            ratePlanId: document.getElementById('profile').value
        };

        try {
            const response = await fetch('/BillingSystem/api/customers', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                alert('Customer added successfully!');
                window.location.href = 'index.html';
            } else {
                const error = await response.json();
                alert('Error: ' + error.message);
            }
        } catch (error) {
            console.error('Error adding customer:', error);
            alert('Error adding customer. Please try again.');
        }
    });

    // Load rate plans when the page loads
    loadRatePlans();
}

// Search Customer Form Handler
if (document.getElementById('searchForm')) {
    document.getElementById('searchForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const phoneNumber = document.getElementById('phoneNumber').value;
        
        try {
            const response = await fetch(`/BillingSystem/api/customers/${phoneNumber}`);
            const customer = await response.json();
            
            if (response.ok) {
                document.getElementById('customerDetails').style.display = 'block';
                document.getElementById('customerName').textContent = customer.name;
                document.getElementById('customerPhone').textContent = customer.phoneNumber;
                document.getElementById('customerEmail').textContent = customer.email;
                document.getElementById('customerAddress').textContent = customer.address;
                document.getElementById('customerProfile').textContent = customer.ratePlan ? customer.ratePlan.name : 'N/A';
            } else {
                alert('Customer not found');
            }
        } catch (error) {
            console.error('Error searching customer:', error);
            alert('Error searching customer. Please try again.');
        }
    });
} 