// Invoice Management JavaScript

if (document.getElementById('searchInvoiceForm')) {
    document.getElementById('searchInvoiceForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const phoneNumber = document.getElementById('customerPhone').value;
        const date = document.getElementById('invoiceDate').value;
        
        // Validation
        if (!phoneNumber || !date) {
            alert('Please enter a phone number and select a date.');
            return;
        }
        
        try {
            // Show loading indicator
            const loadingIndicator = document.getElementById('loadingIndicator');
            if (loadingIndicator) {
                loadingIndicator.style.display = 'block';
            }
            
            // Fixed: Match the actual backend endpoint structure
            const response = await fetch(`/BillingSystem/api/invoice/customer?msisdn=${encodeURIComponent(phoneNumber)}&date=${encodeURIComponent(date)}`);
            
            // Hide loading indicator
            if (loadingIndicator) {
                loadingIndicator.style.display = 'none';
            }
            
            if (response.ok) {
                const invoice = await response.json();
                
                // The backend now returns a single InvoiceViewDTO, not an array of old Invoice objects
                displaySingleInvoice(invoice);
            } else {
                // Attempt to read error message from backend
                const errorText = await response.text();
                alert(`Invoice not found or error occurred: ${errorText}`);
                document.getElementById('invoiceSection').style.display = 'none';
                document.getElementById('invoiceList').style.display = 'none'; // Hide the list too
                document.getElementById('invoiceResult').style.display = 'none'; // Hide result div
            }
        } catch (error) {
            console.error('Error fetching invoice:', error);
            alert('Error fetching invoice. Please try again.');
            // Hide loading indicator
            if (loadingIndicator) {
                loadingIndicator.style.display = 'none';
            }
            document.getElementById('invoiceSection').style.display = 'none';
            document.getElementById('invoiceList').style.display = 'none'; // Hide the list too
            document.getElementById('invoiceResult').style.display = 'none'; // Hide result div
        }
    });
}

// Function to display single invoice breakdown (matching your existing structure)
function displaySingleInvoice(invoice) {
    // Hide invoice list, show detailed invoice section
    const invoiceList = document.getElementById('invoiceList');
    if (invoiceList) { invoiceList.style.display = 'none'; }
    
    const invoiceSection = document.getElementById('invoiceSection');
    if (invoiceSection) { invoiceSection.style.display = 'block'; }
    
    const invoiceResult = document.getElementById('invoiceResult');
    if (invoiceResult) { invoiceResult.style.display = 'block'; }
    
    // Set invoice header
    const invoiceHeader = document.getElementById('invoiceHeader');
    if (invoiceHeader) {
        invoiceHeader.innerHTML = `
            <p>Customer MSISDN: ${invoice.customerMsisdn || 'N/A'}</p>
            <p>Invoice Date: ${invoice.invoiceDate ? invoice.invoiceDate.substring(0, 10) : 'N/A'}</p>
        `;
    }
    
    // Fill breakdown table
    const tbody = document.getElementById('invoiceTable') ? document.getElementById('invoiceTable').querySelector('tbody') : null;
    if (tbody) {
        tbody.innerHTML = '';
        (invoice.breakdown || []).forEach(item => {
            tbody.innerHTML += `
                <tr>
                    <td>${item.serviceType || 'N/A'}</td>
                    <td>${item.totalVolume || 0}</td>
                    <td>${item.totalCharges ? item.totalCharges.toFixed(2) : '0.00'}</td>
                </tr>
            `;
        });
    }
    
    // Show total
    const invoiceTotal = document.getElementById('invoiceTotal');
    if (invoiceTotal) {
        invoiceTotal.innerHTML = `<b>Total Charges: ${invoice.totalCharges ? invoice.totalCharges.toFixed(2) : '0.00'}</b>`;
    }
}

// The following functions (displayInvoices, formatDate, viewInvoice, markAsPaid, clearResults) 
// seem to be related to the old invoice list view and may not be needed anymore 
// if you are only displaying the single detailed invoice view. 
// I will comment them out for now.

/*
// Function to display multiple invoices (if API returns array)
function displayInvoices(invoices) {
    const tableBody = document.getElementById('invoiceTableBody');
    if (!tableBody) return;
    
    tableBody.innerHTML = '';
    
    invoices.forEach(invoice => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${invoice.invoiceNumber || invoice.id || 'N/A'}</td>
            <td>${formatDate(invoice.invoiceDate || invoice.issueDate)}</td>
            <td>${formatDate(invoice.dueDate || invoice.invoiceDate)}</td>
            <td>$${(invoice.totalCharges || invoice.total || 0).toFixed(2)}</td>
            <td>${invoice.status || 'PAID'}</td>
            <td>
                <button onclick="viewInvoice('${invoice.id}')" class="button">View</button>
                ${(invoice.status === 'UNPAID' || !invoice.status) ? 
                    `<button onclick="markAsPaid('${invoice.id}')" class="button">Mark as Paid</button>` : 
                    ''}
            </td>
        `;
        tableBody.appendChild(row);
    });
    
    document.getElementById('invoiceList').style.display = 'block';
    document.getElementById('invoiceSection').style.display = 'none';
}

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString();
}

async function viewInvoice(invoiceId) {
    try {
        const response = await fetch(`/BillingSystem/api/invoices/${invoiceId}`);
        
        if (response.ok) {
            const invoice = await response.json();
            
            // Open PDF in new window if available
            if (invoice.pdfPath) {
                window.open(`/BillingSystem/${invoice.pdfPath}`, '_blank');
            } else {
                // If no PDF, show invoice details in current view
                displaySingleInvoice(invoice);
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
    if (!confirm('Are you sure you want to mark this invoice as paid?')) {
        return;
    }
    
    try {
        const response = await fetch(`/BillingSystem/api/invoices/${invoiceId}/mark-paid`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (response.ok) {
            alert('Invoice marked as paid successfully');
            // Refresh the invoice search
            document.getElementById('searchInvoiceForm').dispatchEvent(new Event('submit'));
        } else {
            const errorText = await response.text();
            alert(`Error marking invoice as paid: ${errorText}`);
        }
    } catch (error) {
        console.error('Error marking invoice as paid:', error);
        alert('Error marking invoice as paid. Please try again.');
    }
}

// Utility function to clear results
function clearResults() {
    document.getElementById('invoiceSection').style.display = 'none';
    document.getElementById('invoiceList').style.display = 'none';
    document.getElementById('invoiceResult').style.display = 'none';
}

// Add event listener to clear results when form is reset
if (document.getElementById('searchInvoiceForm')) {
    const form = document.getElementById('searchInvoiceForm');
    if (form.querySelector('button[type="reset"]')) {
        form.querySelector('button[type="reset"]').addEventListener('click', clearResults);
    }
}
*/