package ru.gothmog.csvtodb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import ru.gothmog.csvtodb.model.Employee;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class EmployeeDaoImpl extends JdbcDaoSupport implements EmployeeDao{

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void insert(List<? extends Employee> employees) {
        String sql = "INSERT INTO employee " + "( first_name, last_name) VALUES ( ?, ?)";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Employee employee = employees.get(i);
                ps.setString(1, employee.getFirstName());
                ps.setString(2, employee.getLastName());
            }

            public int getBatchSize() {
                return employees.size();
            }
        });
    }

    @Override
    public List<Employee> loadAllEmployees() {
        String sql = "SELECT * FROM employee";
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        List<Employee> result = new ArrayList<Employee>();
        for (Map<String, Object> row : rows) {
            Employee employee = new Employee();
            Integer ll = (Integer) row.get("id");
            employee.setId(ll.longValue());
            employee.setFirstName((String) row.get("first_name"));
            employee.setLastName((String) row.get("last_name"));
            result.add(employee);
        }
        return result;
    }
}
