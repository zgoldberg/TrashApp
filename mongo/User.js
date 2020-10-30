var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://localhost:27017/myDatabase');

var Schema = mongoose.Schema;

var userSchema = new Schema({
	username: {type: String, required: true, unique: true},
	fullname: {type: String, required: true},
	password: {type: String, required: true},
	zipcode:  Number,
	admin: Number,
	postsMade: Number,
	postsTagged: Number,
	commentsMade: Number,
	commentsTagged: Number,
	upVotes: Number,
	downVotes: Number
});

// export userSchema as a class called Person
module.exports = mongoose.model('User', userSchema);

userSchema.methods.standardizeName = function() {
    this.username = this.username.toLowerCase();
    return this.username;
}
