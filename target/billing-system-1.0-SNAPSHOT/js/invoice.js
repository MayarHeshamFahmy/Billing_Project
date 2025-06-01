// Invoice Management JavaScript

if (document.getElementById('searchInvoiceForm')) {
    document.getElementById('searchInvoiceForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const phoneNumber = document.getElementById('customerPhone').value;
        
        try {
            const response = await fetch(`/BillingSystem/api/invoices/customer/${phoneNumber}`);
            const invoices = await response.json();
            
            if (response.ok) {
                displayInvoices(invoices);
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
            <td>${formatDate(invoice.issueDate)}</td>
            <td>${formatDate(invoice.dueDate)}</td>
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

async function viewInvoice(invoiceId) {
    try {
        const response = await fetch(`/BillingSystem/api/invoices/${invoiceId}`);
        const invoice = await response.json();
        
        if (response.ok) {
            // Open PDF in new window if available
            if (invoice.pdfPath) {
                window.open(`/BillingSystem/${invoice.pdfPath}`, '_blank');
            } else {
                alert('PDF not available for this invoice');
            }
        } else {
            alert('Error viewing invoice');
        }
    } catch (error) {
        console.error('Error viewing invoice:', error);
        alert('Error viewing invoice. Please try again.');
    }
}

async function markAsPaid(invoiceId) {
    try {
        const response = await fetch(`/BillingSystem/api/invoices/${invoiceId}/mark-paid`, {
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
        alert('Error marking invoice as paid. Please try again.');
    }
} 