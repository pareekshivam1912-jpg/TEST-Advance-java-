CREATE TABLE IF NOT EXISTS Employee (
    ID INT PRIMARY KEY,
    Name VARCHAR(50),
    Department VARCHAR(50),
    Salary DECIMAL(10,2)
);


INSERT INTO Employee (ID, Name, Department, Salary) VALUES
(1, 'Amit', 'IT', 50000),
(2, 'Rahul', 'HR', 40000),
(3, 'Priya', 'IT', 80000),
(4, 'Neha', 'HR', 60000),
(5, 'Karan', 'IT', 80000),
(6, 'Simran', 'Sales', 45000),
(7, 'Mohit', 'Sales', 55000);


SELECT MAX(Salary) 
FROM Employee 
WHERE Salary < (SELECT MAX(Salary) FROM Employee);



SELECT Department, MAX(Salary) 
FROM Employee 
GROUP BY Department;



SELECT * 
FROM Employee e 
WHERE Salary > (
    SELECT AVG(Salary) 
    FROM Employee 
    WHERE Department = e.Department );





SELECT Department, COUNT(*) 
FROM Employee 
GROUP BY Department 
HAVING COUNT(*) > 1;



SELECT * 
FROM Employee 
WHERE Salary IN (
    SELECT Salary 
    FROM Employee 
    GROUP BY Salary 
    HAVING COUNT(*) > 1);


SELECT DISTINCT Salary 
FROM Employee 
ORDER BY Salary DESC 
LIMIT 1 OFFSET 2; --in this it will skip top 2 and pick 3 one 




SELECT * 
FROM Employee 
WHERE Salary = (SELECT MAX(Salary) FROM Employee);