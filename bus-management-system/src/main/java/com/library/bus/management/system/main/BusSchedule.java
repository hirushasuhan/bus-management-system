/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.library.bus.management.system.main;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author HP
 */
public class BusSchedule {
    private int scheduleId;
    private int busId;
    private int routeId;
    private Time departureTime;
    private Time arrivalTime;
    private Date scheduleDate;
    private int availableSeats;
    private Timestamp createdAt;
    private String createdBy;
    
    private String busNumber;
    private String routeName;
    private String source;
    private String destination;
    private String busType;
    
    private static Connection connection = DatabaseConnection.getConnection();
    
    public BusSchedule() {}
    
    public BusSchedule(int busId, int routeId, Time departureTime, 
                      Time arrivalTime, Date scheduleDate, int availableSeats) {
        this.busId = busId;
        this.routeId = routeId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.scheduleDate = scheduleDate;
        this.availableSeats = availableSeats;
        this.createdBy = "Hirusha suhan";
    }
    
    
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    
    public int getBusId() { return busId; }
    public void setBusId(int busId) { this.busId = busId; }
    
    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }
    
    public Time getDepartureTime() { return departureTime; }
    public void setDepartureTime(Time departureTime) { this.departureTime = departureTime; }
    
    public Time getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(Time arrivalTime) { this.arrivalTime = arrivalTime; }
    
    public Date getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(Date scheduleDate) { this.scheduleDate = scheduleDate; }
    
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }
    
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public String getBusType() { return busType; }
    public void setBusType(String busType) { this.busType = busType; }
    
    public boolean save() {
        if (this.scheduleId == 0) {
            return addSchedule();
        } else {
            return updateSchedule();
        }
    }
    
    private boolean addSchedule() {
        String sql = "INSERT INTO bus_schedule (bus_id, route_id, departure_time, arrival_time, schedule_date, available_seats, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, this.busId);
            stmt.setInt(2, this.routeId);
            stmt.setTime(3, this.departureTime);
            stmt.setTime(4, this.arrivalTime);
            stmt.setDate(5, this.scheduleDate);
            stmt.setInt(6, this.availableSeats);
            stmt.setString(7, "hirushasuhan");
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.scheduleId = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding schedule: " + e.getMessage());
        }
        return false;
    }
    
    private boolean updateSchedule() {
        String sql = "UPDATE bus_schedule SET bus_id=?, route_id=?, departure_time=?, arrival_time=?, schedule_date=?, available_seats=? WHERE schedule_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, this.busId);
            stmt.setInt(2, this.routeId);
            stmt.setTime(3, this.departureTime);
            stmt.setTime(4, this.arrivalTime);
            stmt.setDate(5, this.scheduleDate);
            stmt.setInt(6, this.availableSeats);
            stmt.setInt(7, this.scheduleId);
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating schedule: " + e.getMessage());
            return false;
        }
    }
    
    public boolean delete() {
        String sql = "DELETE FROM bus_schedule WHERE schedule_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, this.scheduleId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting schedule: " + e.getMessage());
            return false;
        }
    }
    
    public static List<BusSchedule> searchSchedule(String source, String destination, Date date) {
        List<BusSchedule> schedules = new ArrayList<>();
        String sql = "SELECT s.*, b.bus_number, b.bus_type, r.route_name, r.source, r.destination " +
                    "FROM bus_schedule s " +
                    "JOIN buses b ON s.bus_id = b.bus_id " +
                    "JOIN routes r ON s.route_id = r.route_id " +
                    "WHERE LOWER(r.source) = LOWER(?) AND LOWER(r.destination) = LOWER(?) AND s.schedule_date = ? " +
                    "AND b.status = 'active' " +
                    "ORDER BY s.departure_time";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, source);
            stmt.setString(2, destination);
            stmt.setDate(3, date);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                BusSchedule schedule = new BusSchedule();
                schedule.setScheduleId(rs.getInt("schedule_id"));
                schedule.setBusId(rs.getInt("bus_id"));
                schedule.setRouteId(rs.getInt("route_id"));
                schedule.setDepartureTime(rs.getTime("departure_time"));
                schedule.setArrivalTime(rs.getTime("arrival_time"));
                schedule.setScheduleDate(rs.getDate("schedule_date"));
                schedule.setAvailableSeats(rs.getInt("available_seats"));
                schedule.setBusNumber(rs.getString("bus_number"));
                schedule.setBusType(rs.getString("bus_type"));
                schedule.setRouteName(rs.getString("route_name"));
                schedule.setSource(rs.getString("source"));
                schedule.setDestination(rs.getString("destination"));
                schedule.setCreatedAt(rs.getTimestamp("created_at"));
                schedule.setCreatedBy(rs.getString("created_by"));
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            System.err.println("Error searching schedules: " + e.getMessage());
        }
        return schedules;
    }
    
    public static List<BusSchedule> getAllSchedules() {
        List<BusSchedule> schedules = new ArrayList<>();
        String sql = "SELECT s.*, b.bus_number, b.bus_type, r.route_name, r.source, r.destination " +
                    "FROM bus_schedule s " +
                    "JOIN buses b ON s.bus_id = b.bus_id " +
                    "JOIN routes r ON s.route_id = r.route_id " +
                    "ORDER BY s.schedule_date DESC, s.departure_time";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                BusSchedule schedule = new BusSchedule();
                schedule.setScheduleId(rs.getInt("schedule_id"));
                schedule.setBusId(rs.getInt("bus_id"));
                schedule.setRouteId(rs.getInt("route_id"));
                schedule.setDepartureTime(rs.getTime("departure_time"));
                schedule.setArrivalTime(rs.getTime("arrival_time"));
                schedule.setScheduleDate(rs.getDate("schedule_date"));
                schedule.setAvailableSeats(rs.getInt("available_seats"));
                schedule.setBusNumber(rs.getString("bus_number"));
                schedule.setBusType(rs.getString("bus_type"));
                schedule.setRouteName(rs.getString("route_name"));
                schedule.setSource(rs.getString("source"));
                schedule.setDestination(rs.getString("destination"));
                schedule.setCreatedAt(rs.getTimestamp("created_at"));
                schedule.setCreatedBy(rs.getString("created_by"));
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving schedules: " + e.getMessage());
        }
        return schedules;
    }
    
    public static List<BusSchedule> getSchedulesByDate(Date date) {
        List<BusSchedule> schedules = new ArrayList<>();
        String sql = "SELECT s.*, b.bus_number, b.bus_type, r.route_name, r.source, r.destination " +
                    "FROM bus_schedule s " +
                    "JOIN buses b ON s.bus_id = b.bus_id " +
                    "JOIN routes r ON s.route_id = r.route_id " +
                    "WHERE s.schedule_date = ? " +
                    "ORDER BY s.departure_time";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, date);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                BusSchedule schedule = new BusSchedule();
                schedule.setScheduleId(rs.getInt("schedule_id"));
                schedule.setBusId(rs.getInt("bus_id"));
                schedule.setRouteId(rs.getInt("route_id"));
                schedule.setDepartureTime(rs.getTime("departure_time"));
                schedule.setArrivalTime(rs.getTime("arrival_time"));
                schedule.setScheduleDate(rs.getDate("schedule_date"));
                schedule.setAvailableSeats(rs.getInt("available_seats"));
                schedule.setBusNumber(rs.getString("bus_number"));
                schedule.setBusType(rs.getString("bus_type"));
                schedule.setRouteName(rs.getString("route_name"));
                schedule.setSource(rs.getString("source"));
                schedule.setDestination(rs.getString("destination"));
                schedule.setCreatedAt(rs.getTimestamp("created_at"));
                schedule.setCreatedBy(rs.getString("created_by"));
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving schedules by date: " + e.getMessage());
        }
        return schedules;
    }
    
    @Override
    public String toString() {
        return String.format("Schedule{ID=%d, Bus='%s', Route='%s', %s->%s, Dep=%s, Arr=%s, Date=%s, Seats=%d}",
                scheduleId, busNumber, routeName, source, destination, 
                departureTime, arrivalTime, scheduleDate, availableSeats);
    }
}
