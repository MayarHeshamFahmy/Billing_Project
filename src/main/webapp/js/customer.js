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
        // Fetch customer details
        let customer;
        try {
            const response = await fetch(`/BillingSystem/api/customers/${phoneNumber}`);
            customer = await response.json();
            if (response.ok) {
                document.getElementById('customerDetails').style.display = 'block';
                document.getElementById('customerName').textContent = customer.name;
                document.getElementById('customerPhone').textContent = customer.phoneNumber;
                document.getElementById('customerEmail').textContent = customer.email;
                document.getElementById('customerAddress').textContent = customer.address;
                document.getElementById('customerProfile').textContent = customer.ratePlan ? customer.ratePlan.name : 'N/A';
                // Hide invoice section if it was visible from a previous search on invoice page
                const invoiceSection = document.getElementById('invoiceSection');
                if (invoiceSection) {
                    invoiceSection.style.display = 'none';
                }
            } else {
                alert('Customer not found');
                document.getElementById('customerDetails').style.display = 'none';
                const invoiceSection = document.getElementById('invoiceSection');
                if (invoiceSection) {
                     invoiceSection.style.display = 'none';
                }
                return;
            }
        } catch (error) {
            alert('Error searching customer. Please try again.');
            const invoiceSection = document.getElementById('invoiceSection');
             if (invoiceSection) {
                 invoiceSection.style.display = 'none';
             }
            return;
        }
    });
}

// Add invoice search logic
if (document.getElementById('invoiceSearchForm')) {
    document.getElementById('invoiceSearchForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const msisdn = window.currentCustomerMsisdn;
        const date = document.getElementById('invoiceDate').value;
        if (!msisdn || !date) {
            alert('Please search for a customer and select a date.');
            return;
        }
        const response = await fetch(`/BillingSystem/api/invoice/customer?msisdn=${encodeURIComponent(msisdn)}&date=${encodeURIComponent(date)}`);
        if (!response.ok) {
            alert('Invoice not found or error occurred.');
            document.getElementById('invoiceResult').style.display = 'none';
            return;
        }
        const invoice = await response.json();
        // Show invoice header
        document.getElementById('invoiceHeader').innerHTML = `
            <p>Customer MSISDN: ${invoice.customerMsisdn}</p>
            <p>Invoice Date: ${invoice.invoiceDate ? invoice.invoiceDate.substring(0, 10) : ''}</p>
        `;
        // Fill table
        const tbody = document.getElementById('invoiceTable').querySelector('tbody');
        tbody.innerHTML = '';
        (invoice.breakdown || []).forEach(item => {
            tbody.innerHTML += `
                <tr>
                    <td>${item.serviceType}</td>
                    <td>${item.totalVolume}</td>
                    <td>${item.totalCharges ? item.totalCharges.toFixed(2) : '0.00'}</td>
                </tr>
            `;
        });
        // Show total
        document.getElementById('invoiceTotal').innerHTML = `<b>Total Charges: ${invoice.totalCharges ? invoice.totalCharges.toFixed(2) : '0.00'}</b>`;
        // Show the invoice result section
        document.getElementById('invoiceResult').style.display = 'block';
    });
} 