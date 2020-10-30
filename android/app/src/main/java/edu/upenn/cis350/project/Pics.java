package edu.upenn.cis350.project;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.UnknownHostException;
//import com.mongodb.DB;
//import com.mongodb.DBCollection;
//import com.mongodb.DBCursor;
//import com.mongodb.Mongo;
//import com.mongodb.MongoException;
//import com.mongodb.gridfs.GridFS;
//import com.mongodb.gridfs.GridFSDBFile;
//import com.mongodb.gridfs.GridFSInputFile;
//
//public class Pics extends Post {
//
//String [posts.lenght] fds;
//    public savePicToDB(Post post, String fd){
//      Mongo mongo = new Mongo("localhost", 27017);
//			DB db = mongo.getDB("imagedb");
//			DBCollection collection = db.getCollection("images");
//
//      File imageFile = new File(fd);
//      GridFS gfsPhoto = new GridFS(db, "photo" + post.coordX.toString()
//       + post.coordY.toString() +user);
//
//			// get image file from local drive
//			GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
//
//			// set a new filename for identify purpose
//			gfsFile.setFilename(newFileName);
//
//			// save the image file into mongoDB
//			gfsFile.save();
//    }
//
//    void printList(){
//      DBCursor cursor = gfsPhoto.getFileList();
//			while (cursor.hasNext()) {
//				System.out.println(cursor.next());
//			}
//    }
//
//    void RmPic(Post post, String fd){
//      GridFSDBFile imageForOutput = gfsPhoto.findOne(fd);
//      gfsPhoto.remove(gfsPhoto.findOne(fd));
//
//    }
//}
