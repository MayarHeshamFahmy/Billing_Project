document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchForm');
    const dashboardContent = document.getElementById('dashboardContent');
    
    searchForm.addEventListener('submit', function(e) {
        e.preventDefault();
        const phoneNumber = document.getElementById('phoneNumber').value;
        loadCustomerData(phoneNumber);
    });

    function loadCustomerData(phoneNumber) {
        // Show loading state
        dashboardContent.style.display = 'block';
        
        // Fetch customer data
        fetch(`/api/customers/${phoneNumber}/dashboard`)
            .then(response => response.json())
            .then(data => {
                updateDashboard(data);
            })
            .catch(error => {
                console.error('Error loading dashboard:', error);
                alert('Error loading dashboard data. Please try again.');
            });
    }

    function updateDashboard(data) {
        // Update balance
        document.getElementById('currentBalance').textContent = 
            `$${data.balance.toFixed(2)}`;
        document.getElementById('balanceProgress').style.width = 
            `${Math.min((data.balance / data.balanceLimit) * 100, 100)}%`;

        // Update free units
        document.getElementById('remainingUnits').textContent = 
            data.remainingFreeUnits;
        document.getElementById('unitsProgress').style.width = 
            `${Math.min((data.remainingFreeUnits / data.totalFreeUnits) * 100, 100)}%`;

        // Update subscriptions
        const subscriptionsList = document.getElementById('subscriptionsList');
        subscriptionsList.innerHTML = data.subscriptions.map(sub => `
            <div class="activity-item">
                <h3>${sub.packageName}</h3>
                <p>Free Units: ${sub.remainingFreeUnits}/${sub.totalFreeUnits}</p>
                <p>Status: ${sub.active ? 'Active' : 'Inactive'}</p>
            </div>
        `).join('');

        // Update recent activity
        const recentActivity = document.getElementById('recentActivity');
        recentActivity.innerHTML = data.recentActivity.map(activity => `
            <div class="activity-item">
                <p><strong>${activity.type}</strong></p>
                <p>${activity.description}</p>
                <small>${new Date(activity.timestamp).toLocaleString()}</small>
            </div>
        `).join('');

        // Update invoices
        const invoicesTableBody = document.getElementById('invoicesTableBody');
        invoicesTableBody.innerHTML = data.invoices.map(invoice => `
            <tr>
                <td>${invoice.invoiceNumber}</td>
                <td>${new Date(invoice.issueDate).toLocaleDateString()}</td>
                <td>$${invoice.total.toFixed(2)}</td>
                <td>${invoice.status}</td>
                <td>
                    <button onclick="viewInvoice('${invoice.id}')">View</button>
                    ${invoice.status === 'UNPAID' ? 
                        `<button onclick="payInvoice('${invoice.id}')">Pay</button>` : 
                        ''}
                </td>
            </tr>
        `).join('');
    }

    // Function to view invoice details
    window.viewInvoice = function(invoiceId) {
        window.location.href = `/invoice.html?id=${invoiceId}`;
    };

    // Function to handle invoice payment
    window.payInvoice = function(invoiceId) {
        if (confirm('Do you want to pay this invoice now?')) {
            fetch(`/api/invoices/${invoiceId}/pay`, {
                method: 'POST'
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Payment successful!');
                    // Reload dashboard data
                    loadCustomerData(document.getElementById('phoneNumber').value);
                } else {
                    alert('Payment failed: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error processing payment:', error);
                alert('Error processing payment. Please try again.');
            });
        }
    };
}); 