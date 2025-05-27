# Billing System Database

This directory contains the database scripts for the Billing System.

## Files

- `schema.sql`: Contains the database schema creation scripts
- `data.sql`: Contains sample data for testing

## Database Structure

### Tables

1. **profiles**
   - Stores different customer profiles (Basic, Premium, Business)
   - Contains base pricing for each profile

2. **service_packages**
   - Groups related services together
   - Defines package pricing and free units
   - Links to profiles

3. **services**
   - Individual services (Voice, SMS, Data)
   - Contains unit pricing and type
   - Links to service packages

4. **customers**
   - Customer information
   - Links to profiles
   - Contains contact details

5. **service_subscriptions**
   - Tracks customer subscriptions to service packages
   - Manages free units allocation
   - Tracks subscription status

6. **cdrs**
   - Call Detail Records
   - Stores usage information
   - Links to customers and invoices

7. **invoices**
   - Customer invoices
   - Contains billing information
   - Tracks payment status

## Usage

1. Create the database:
```sql
mysql -u root -p < schema.sql
```

2. Load sample data:
```sql
mysql -u root -p billing_system < data.sql
```

## Notes

- All monetary values are stored as DECIMAL(10,2)
- Dates are stored in DATETIME format
- Foreign keys ensure data integrity
- Indexes are automatically created for primary keys 