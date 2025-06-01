// Invoice Management JavaScript

if (document.getElementById('searchInvoiceForm')) {
    document.getElementById('searchInvoiceForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const phoneNumber = document.getElementById('customerPhone').value;
        
        try {
            const response = await fetch(`/api/customers/${phoneNumber}/dashboard`);
            const data = await response.json();
            
            if (response.ok) {
                displayInvoices(data.invoices);
            } else {
                alert('No invoices found for this customer');
            }
        } catch (error) {
            console.error('Error fetching invoices:', error);
            alert('Error fetching invoices. Please try again.');
        }
    });
}

function displayInvoices(invoices) {
    const tableBody = document.getElementById('invoiceTableBody');
    tableBody.innerHTML = '';
    
    invoices.forEach(invoice => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${invoice.invoiceNumber}</td>
            <td>${new Date(invoice.issueDate).toLocaleDateString()}</td>
            <td>${new Date(invoice.dueDate).toLocaleDateString()}</td>
            <td>$${invoice.total.toFixed(2)}</td>
            <td>${invoice.status}</td>
            <td>
                <button onclick="viewInvoice('${invoice.id}')" class="button">View</button>
                ${invoice.status === 'UNPAID' ? 
                    `<button onclick="markAsPaid('${invoice.id}')" class="button">Mark as Paid</button>` : 
                    ''}
            </td>
        `;
        tableBody.appendChild(row);
    });
    
    document.getElementById('invoiceList').style.display = 'block';
}

function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString();
}

async function viewInvoice(id) {
    try {
        const response = await fetch(`/api/invoices/${id}`);
        if (response.ok) {
            const invoice = await response.json();
            // Handle viewing invoice details
            alert(`Viewing invoice ${invoice.invoiceNumber}`);
        } else {
            alert('Error loading invoice details');
        }
    } catch (error) {
        console.error('Error viewing invoice:', error);
        alert('Error viewing invoice details');
    }
}

async function markAsPaid(id) {
    if (confirm('Are you sure you want to mark this invoice as paid?')) {
        try {
            const response = await fetch(`/api/invoices/${id}/pay`, {
                method: 'POST'
            });
            if (response.ok) {
                alert('Invoice marked as paid successfully');
                // Refresh the invoice list
                document.getElementById('searchInvoiceForm').dispatchEvent(new Event('submit'));
            } else {
                alert('Error marking invoice as paid');
            }
        } catch (error) {
            console.error('Error marking invoice as paid:', error);
            alert('Error marking invoice as paid');
        }
    }
} 