var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://localhost:27017/myDatabase');

var Schema = mongoose.Schema;

var commentsSchema = new Schema({
	user: {type: String, required: true},
	text: {type: String, required: true},
	image: {type: String}
	votesUp: Number
	votesDown: Number
    });

// export personSchema as a class called Person
module.exports = mongoose.model('Comment', commentSchema);
