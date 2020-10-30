var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://localhost:27017/myDatabase');

var Schema = mongoose.Schema;

var postSchema = new Schema({
	user: {type: String, required: true},
	text: {type: String, required: true},
	image: {type: String, required:true},
	votesUp: Number,
	votesDown: Number,
	long: Number,
	lat: Number,
	comments : [{user: {type: String, required: true},
			text: {type: String, required: true},
			image: {type: String},
			votesUp: Number,
			votesDown: Number,
			long: Number,
			lat: Number}]
    });

// export personSchema as a class called Person
module.exports = mongoose.model('Post', postSchema);
