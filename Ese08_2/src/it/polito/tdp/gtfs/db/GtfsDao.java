package it.polito.tdp.gtfs.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.onebusaway.gtfs.model.Agency;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.gtfs.model.Stop;

public class GtfsDao {

	/****
	 * Agency Methods
	 ****/

	public Collection<Agency> getAllAgencies() {
		String sql = "SELECT * FROM gtfs_agencies";

		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);

			List<Agency> agencies = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {

				Agency agency = buildAgency(res);
				agencies.add(agency);

			}

			st.close();
			conn.close();

			return agencies;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error", e);
		}

	}

	public Agency getAgencyForId(String id) {
		String sql = "SELECT * FROM gtfs_agencies WHERE id=?";

		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, id);
			ResultSet res = st.executeQuery();

			Agency agency = null;
			if (res.next()) {

				agency = buildAgency(res);

			}

			st.close();
			conn.close();

			return agency;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error", e);
		}

	}

	/*-- Helper methods for Agencies --*/

	private Agency buildAgency(ResultSet res) throws SQLException {
		Agency agency = new Agency();

		agency.setId(res.getString("id"));
		agency.setName(res.getString("name"));
		agency.setLang(res.getString("lang"));
		// TODO add other fields

		return agency;
	}

	/****
	 * {@link Stop} Methods
	 ****/

	public Collection<Stop> getAllStops(Agency agency) {
		String sql = "SELECT * FROM gtfs_stops WHERE agencyId=?";

		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, agency.getId());
			List<Stop> stops = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {

				Stop stop = buildStop(res);
				stops.add(stop);

			}

			st.close();
			conn.close();

			return stops;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error", e);
		}

	}


	/****
	 * High Level Methods
	 ****/
	

	/*-- Helper methods for Stop --*/

	private Stop buildStop(ResultSet res) throws SQLException {
		Stop stop = new Stop();

		stop.setId(new AgencyAndId(res.getString("agencyId"), res.getString("id")));
		stop.setName(res.getString("name"));
		// TODO add other fields

		return stop;
	}
	
	public Collection<Stop> getNextStops(Stop stop1){
		
		String sql = "SELECT DISTINCT stop2.*  FROM gtfs_stops AS stop1, gtfs_stop_times AS stoptimes1, gtfs_trips,  "
				+ "gtfs_stops AS stop2, gtfs_stop_times AS stoptimes2  "
				+ "WHERE   stop1.agencyId=? AND stop1.id=? "
				+ "AND stoptimes2.stopSequence = stoptimes1.stopSequence+1  "
				+ " AND stop1.agencyId=stoptimes1.stop_agencyId "
				+ " AND stop1.id=stoptimes1.stop_id  "
				+ "AND stoptimes1.trip_agencyId=gtfs_trips.agencyId  "
				+ "AND stoptimes1.trip_id=gtfs_trips.id  "
				+ "AND stop2.agencyId=stoptimes2.stop_agencyId  "
				+ "AND stop2.id=stoptimes2.stop_id  "
				+ "AND stoptimes2.trip_agencyId=gtfs_trips.agencyId  "
				+ "AND stoptimes2.trip_id=gtfs_trips.id";
		
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, stop1.getId().getAgencyId());
			st.setString(2, stop1.getId().getId());
			
			
			ResultSet res = st.executeQuery();

			Collection<Stop> stops = new LinkedList<Stop>();
			while (res.next()){ 
				Stop temp = this.buildStop(res);
				stops.add(temp);
		}
			st.close();
			conn.close();

			return stops ;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error", e);
		}


	}
	
	// Testing method
	
	public static void main(String [] args) {
		
		GtfsDao dao = new GtfsDao() ;
		
		Collection<Agency> agencies = dao.getAllAgencies() ;
		for( Agency a : agencies ) {
			System.out.format("Agency %s: %s\n", a.getId(), a.getName()) ;
			
			Collection<Stop> stops = dao.getAllStops(a) ;
			
			for( Stop s: stops) {
				if(!s.getId().toString().endsWith("_B")){
				System.out.format("\tStop %s: %s\n", s.getId(), s.getName()) ;
				System.out.println("Stazioni direttamente connesse");
				Collection<Stop> conn = dao.getNextStops(s);
				for(Stop tmp: conn){
					System.out.format("\tStop %s: %s\n", tmp.getId(), tmp.getName()) ;
				}
				System.out.println("FINITO");
			}
			
		}
	}
		
	
		
	}
}
