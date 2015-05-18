SELECT * 
FROM gtfs_stops AS stop1, gtfs_stop_times AS stoptimes1, gtfs_trips,
gtfs_stops AS stop2, gtfs_stop_times AS stoptimes2
WHERE 

stop1.agencyId=? AND stop1.id=?
AND stop2.agencyId=? AND stop2.id=?

AND stoptimes2.stopSequence = stoptimes1.stopSequence+1

AND stop1.agencyId=stoptimes1.stop_agencyId
AND stop1.id=stoptimes1.stop_id
AND stoptimes1.trip_agencyId=gtfs_trips.agencyId
AND stoptimes1.trip_id=gtfs_trips.id

AND stop2.agencyId=stoptimes2.stop_agencyId
AND stop2.id=stoptimes2.stop_id
AND stoptimes2.trip_agencyId=gtfs_trips.agencyId
AND stoptimes2.trip_id=gtfs_trips.id