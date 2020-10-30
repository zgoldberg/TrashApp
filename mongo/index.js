// set up Express
var express = require('express');
var app = express();
var router =express.Router();
// set up EJS
app.set('view engine', 'ejs');

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.json({ limit: "50mb" }))
app.use(bodyParser.urlencoded({ limit: "50mb", extended: true, parameterLimit: 50000 }))
var User = require('./User.js');
var Post = require('./Post.js');
/***************************************/
var MongoClient = require('mongodb').MongoClient;
var url = "mongodb://localhost:27017/";
// route for creating a new person
// this is the action of the "create new person" form
app.use('/create', (req, res) => {
	// construct the Person from the form data which is in the request body
	var newUser = new User ({
		username: req.query.name,
		zipcode: req.query.zipcode,
		password: req.query.password,
		fullname: req.query.fullname,
		admin: 0,
		postsMade: 0,
		postsTagged: 0,
		commentsMade: 0,
		commentsTagged: 0,
		upVotes: 0,
		downVotes: 0


	    });

	// save the person to the database
	newUser.save( (err) => {
		if (err) {
		    res.type('html').status(404);
		    res.write('uh oh: ' + err);
		    console.log(err);
		    res.end();
		}
		else {
		    // display the "successfull created" page using EJS
		    res.render('created', {user : newUser});
		}
	    } );
    }
    );


app.use('/update', (req, res) => {


	// construct the Person from the form data which is in the request body
	var user = req.body.name;
	// save the person to the database
	var exist = 0;
	var user =  req.body.name;
	var zip = req.body.zipcode;
	var pass =  req.body.password;
	var f = req.body.fullname;
	MongoClient.connect(url, function(err, db) {
		  if (err) throw err;
		  var dbo = db.db("myDatabase");
		  var collection = dbo.collection("users");
		  collection.find({username: user}, {$exists: true}).toArray(function(err, doc){
    if(doc) {
    	if(doc[0]){
    		var myquery = { username: user };
				var newvalues = { $set: {username: user, password: pass, fullname: f, zipcode: zip} };
		  	dbo.collection("users").updateOne(myquery, newvalues, function(err, obj) {
		    if (err) throw res.type('html').status(405);
		    console.log("dood got changed");

		    db.close();
		    var newUser = new User({username: user, password: pass, fullname: f, zipcode: zip});
		     res.render("updated", {user : newUser} );
		    res.type('html').status(200);
		  });
		  return;
			} else{
				res.type('html').status(404);
      	res.end();
    	}
		} else if(!doc) // if it does not
    {
       res.type('html').status(404);
       res.end();
    }


});
	});
});


app.use('/delete', (req, res) => {


	// construct the Person from the form data which is in the request body
	var user = req.body.name;
	// save the person to the database
	var exist = 0;
	MongoClient.connect(url, function(err, db) {


		  if (err) throw err;
		  var dbo = db.db("myDatabase");
		  var collection = dbo.collection("users");
		  collection.find({username: user}, {$exists: true}).toArray(function(err, doc){
    if(doc) {
    	if(doc[0]){
    		var myquery = { username: user };
		  	dbo.collection("users").deleteOne(myquery, function(err, obj) {
		    if (err) throw res.type('html').status(405);
		    console.log("1 document deleted");
		    db.close();
		    res.type('html').status(200);
		    res.end();
		  });
		  return;
			} else{
				res.type('html').status(404);
      	res.end();
    	}
		} else if(!doc) // if it does not
    {
       res.type('html').status(404);
       res.end();
    }
    console.log(exist + " 2");

});
	});
});

app.use('/deleteWeb', (req, res) => {


	// construct the Person from the form data which is in the request body
	var user = req.body.name;
	// save the person to the database
	var exist = 0;
	MongoClient.connect(url, function(err, db) {


		  if (err) throw err;
		  var dbo = db.db("myDatabase");
		  var collection = dbo.collection("users");
		  collection.find({username: user}, {$exists: true}).toArray(function(err, doc){
    if(doc) {
    	if(doc[0]){
    		var myquery = { username: user };
		  	dbo.collection("users").deleteOne(myquery, function(err, obj) {
		    if (err) throw res.type('html').status(405);
		    console.log("1 document deleted");
		    db.close();
		   // res.type('html').status(200);
		   // res.end();
		  });
		  return;
			} else{
				//res.type('html').status(404);
      	//res.end();
    	}
		} else if(!doc) // if it does not
    {
       //res.type('html').status(404);
      // res.end();
    }
     res.redirect('/public/allUsers.html');

});
	});
});


app.use('/createUser', (req, res) => {
	// construct the Person from the form data which is in the request body
		//console.log(req);

	var newUser = new User ({
		username: req.body.name,
		zipcode: req.body.zipcode,
		password: req.body.password,
		fullname: req.body.fullname,
		admin: 0,
		postsMade: 0,
		postsTagged: 0,
		commentsMade: 0,
		commentsTagged: 0,
		upVotes: 0,
		downVotes: 0


	    });

	// save the person to the database
	newUser.save( (err) => {	
		if (err) {
		    res.type('html').status(404);
		    res.write('uh oh: ' + err);
		    //console.log(err);
		    res.end();
		}
		else {
		    // display the "successfull created" page using EJS
		    res.render('created', {user : newUser});
		    res.end();
		}
	    } );
    }
 );

// Jason wrote this, for updating User Stats
app.use('/updateUserStats', (req, res) => {
	console.log(req);
	var updateName = req.body.name;
	var posts = req.body.posts;
	var tags = req.body.tags;
	var up  = req.body.up;
	var down = req.body.down;
	var cPost = req.body.cPost;
	var cTag = req.body.cTag;
	User.findOne( { username: updateName }, (err, user) => {
		if (err) {
			 res.type('html').status(404);
		}
		else if (!user) {
			 res.type('html').status(404);
		}
		
		else {
			user.postsMade = posts;
			user.postsTagged = tags;
			user.commentsMade = cPost
			user.commentsTagged = cTag;
			user.upVotes = up;
			user.downVotes = down;
			user.save( (err) => {
				if (err) {
					 res.type('html').status(404);
				}
			});
		}
		 
	});
	res.type('html').status(200);
	res.end();
});

app.use('/createPost', (req, res) => {
	// construct the Person from the form data which is in the request body
		//console.log(req.body.image);
		var img = "";
		if(req.body.image){
			img = req.body.image;
		}
	var newPost = new Post ({
		user: req.body.user,
		text: req.body.text,
		lat: req.body.lat,
		image : img,
		long: req.body.long,
		voteUp: 0,
		voteDown: 0,
		comments : []
	    });

	// save the person to the database
	newPost.save( (err) => {
		if (err) {
		    res.type('html').status(404);
		    res.write('uh oh: ' + err);
		    //console.log(err);
		    res.end();
		}
		else {
		    // display the "successfull created" page using EJS
		    //res.render('created', {post : newPost});
		    res.end();
		}
	    } );
    }
 );

app.use('/allPostsWeb', (req, res) => {
	// find all the Person objects in the database
	Post.find( {}, (err, posts) => {
		if (err) {
		    res.type('html').status(200);
		    console.log('uh oh' + err);
		    res.write(err);
		}
		else {
		    if (posts.length == 0) {
			res.type('html').status(200);
			res.write('There are no people');
			res.end();
			return;
		    }
		    // use EJS to show all the people
		    console.log(posts);
		    res.render('allPosts', {posts : posts});
		}
	    }).sort({ 'zipcode': 'asc' }); // this sorts them BEFORE rendering the results
    });

app.use('/allPosts', (req, res) => {
	// find all the Person objects in the database
	Post.find( {}, (err, posts) => {
		if (err) {
		    res.type('html').status(200);
		    console.log('uh oh' + err);
		    res.write(err);
		}
		else {
		    if (posts.length == 0) {
			res.type('html').status(200);
			res.write('There are no people');
			res.end();
			return;
		    }
		    // use EJS to show all the people
		    //console.log(posts);
		    res.json(posts);
		}
	    }).sort({ 'zipcode': 'asc' }); // this sorts them BEFORE rendering the results
    });


// route for showing all the people
app.use('/all', (req, res) => {

	// find all the Person objects in the database
	User.find( {}, (err, users) => {
		if (err) {
		    res.type('html').status(200);
		    console.log('uh oh' + err);
		    res.write(err);
		}
		else {
		    if (users.length == 0) {
			res.type('html').status(200);
			res.write('There are no people');
			res.end();
			return;
		    }
		    // use EJS to show all the people
		    res.render('all', { users: users });

		}
	    }).sort({ 'zipcode': 'asc' }); // this sorts them BEFORE rendering the results
    });

app.use('/allUsers', (req, res) => {

	// find all the Person objects in the database
	User.find( {}, (err, users) => {
		if (err) {
		    res.type('html').status(200);
		    console.log('uh oh' + err);
		    res.write(err);
		}
		else {
		    if (users.length == 0) {
			res.type('html').status(200);
			res.write('There are no people');
			res.end();
			return;
		    }
		    // use EJS to show all the people
		   //console.log(users);
		    res.json(users);
		}
	    }).sort({ 'zipcode': 'asc' }); // this sorts them BEFORE rendering the results
    });

// route for accessing data via the web api
// to use this, make a request for /api to get an array of all Person objects
// or /api?name=[whatever] to get a single object
app.use('/api', (req, res) => {
	console.log("LOOKING FOR SOMETHING?");

	// construct the query object
	var queryObject = {};
	if (req.query.name) {
	    // if there's a name in the query parameter, use it here
	    queryObject = { "name" : req.query.username };
	}
	var found  = 0;
	User.find( queryObject, (err, users) => {
		if (err) {
		    console.log('uh oh' + err);
		    res.json({});
		    var found  = 1;
		     	res.end();
		}
		else if (users.length == 0) {
		    // no objects found, so send back empty json
		    res.json({});
		    var found  = 1;
		    res.end();
		}
		else if (users.length == 1 ) {
		    var user = users[0];
		    // send back a single JSON object
		    res.json( user );
 				var found  = 1;
 				res.end();
		}
		else {
		    // construct an array out of the result
		    users.forEach( (user) => {
		    	if(user.username == req.query.name){
		    		console.log(user);
		    		found = 1;
				   	res.json(user);
				   	res.end();
		    	}

			});
		    // send it back as JSON Array
		    //res.json(returnArray);
		}
		  if(!found){
		   res.json( { "name" :"" , "password": null, "fullname" : "","zipcode" :-1} );
		  }
			    });
    });

// This is still incomplete, doesn't extract proper user object
app.use('/updateStats', (req, res) => {
	   console.log(req.query.username);

	// construct the query object
	var queryObject = {};
	    // if there's a name in the query parameter, use it here
	queryObject = { "user" : req.query.username };

	Post.find( queryObject, (err, posts) => {
		if (err) {
		    console.log('uh oh' + err);
		    res.json({});
		}
		else if (posts.length == 0) {
		    // no objects found, so send back empty json
		    res.json({});
		}
		else if (posts.length == 1 ) {
		    var post = posts[0];
		    // send back a single JSON object
		    res.json(post);

		}
		else {
		    // construct an array out of the result
		    var returnArray = [];
		    posts.forEach( (post) => {
		    	if(post.user == req.query.username)
			   	 returnArray.push(post);
			});
		    // send it back as JSON Array
		    res.json(returnArray);
		}

	    });
});


app.use('/findUserPost', (req, res) => {
		console.log("LOOKING FOR Post?");

	// construct the query object
	var queryObject = {};
	    // if there's a name in the query parameter, use it here
	     var returnArray = [];
	queryObject = { "user" : req.body.name };
	//console.log(queryObject);
	Post.find( queryObject, (err, posts) => {
		if (err) {
		    console.log('uh oh' + err);
		    res.json([]);
		}
		else if (posts.length == 0) {
		    // no objects found, so send back empty json

		    res.json([]);
		}
		else if (posts.length == 1 ) {
		    var post = posts[0];
		    // send back a single JSON object
		    res.json(post);
		}
		else {
		    // construct an array out of the result
		    posts.forEach( (post) => {
			   	 		returnArray.push(post);
		    	})
		    // send it back as JSON Array]

		    res.json(returnArray);
		}

	    });
});

app.use('/createadmin', (req, res) => {
	// construct the Person from the form data which is in the request body
	var newUser = new User ({
		username: req.body.name,
		zipcode: req.body.zipcode,
		password: req.body.password,
		fullname: req.body.fullname,
		admin: 1,
		postsMade: 0,
		postsTagged: 0,
		commentsMade: 0,
		commentsTagged: 0,
		upVotes: 0,
		downVotes: 0

	});

	// save the person to the database
	newUser.save( (err) => {
		if (err) {
		    res.type('html').status(404);
		    res.write('uh oh: ' + err);
		    console.log(err);
		    res.end();
		}
		else {
		    // display the "successfull created" page using EJS
		    res.render('created', {user : newUser});
		}
	    } );
    }
    );

	app.use('/getComment', (req, res) => {
		console.log("LOOKING FOR comments?");
	  //console.log(req.query.id);

	// construct the query object
	var ObjectID = require('mongodb').ObjectID;
	queryObject = { "_id" : ObjectID(req.query.id) };
	//console.log(queryObject);
	Post.find( queryObject, (err, posts) => {
		if (err) {
		    console.log('uh oh' + err);
		    res.json([]);
		}
		else if (posts.length == 0) {
		    // no objects found, so send back empty json

		    res.json([]);
		}
		else if (posts.length == 1 ) {
		    var post = posts[0];
		    // send back a single JSON object
		    //console.log(post);
		    //console.log(post.comments);
		    res.json(post.comments);

		}
});
});

app.use('/addComment', (req, res) => {
		console.log("Making post");
	  //console.log(req.body);

	// construct the query object
	var ObjectID = require('mongodb').ObjectID;
	queryObject = { "_id" : ObjectID(req.body.id) };
	var comment = {
		user: req.body.user,
		text: req.body.text,
		lat: req.body.lat,
		long: req.body.long,
		voteUp: 0,
		voteDown: 0,

	};
	MongoClient.connect(url, function(err, db) {
		  if (err){
		  	console.log('uh oh' + err);
		  	throw res.type('html').status(404);
		  }
		  var dbo = db.db("myDatabase");
		  var collection = dbo.collection("posts");
			collection.updateOne(queryObject, {$push: {"comments":
                    comment}});
			});

		Post.find( queryObject, (err, posts) => {
			if (err) {
            console.log('uh oh' + err);
            res.json([]);
        }
        else if (posts.length == 0) {
            // no objects found, so send back empty json

        } else{
       	 	//console.log(posts);
        }
      }
      );
		res.type('html').status(200);
		res.end();
});


app.use('/removePost', (req, res) => {
	console.log(req.body.id);
	var ObjectID = require('mongodb').ObjectID;
	myquery = { "_id" : ObjectID(req.body.id) };


	MongoClient.connect(url, function(err, db) {
		  if (err){
		  	console.log('uh oh' + err);
		  	throw res.type('html').status(404);
		  }
        console.log("LOOKING FOR Post to dlete?");
        var dbo = db.db("myDatabase");
        var collection = dbo.collection("users");

    // construct the query object
		  // if there's a name in the query parameter, use it here
      var returnArray = [];
    //queryObject = { "post" : req.query.id };
    Post.find( myquery, (err, posts) => {
        if (err) {
            console.log('uh oh' + err);
            res.json([]);
        }
        else if (posts.length == 0) {
            // no objects found, so send back empty json


            res.json([]);
        }
        else if (posts.length == 1 ) {
            var post = posts[0];
                dbo.collection("posts").deleteOne(myquery, function(err, obj) {
            if (err) throw res.type('html').status(405);
            console.log("1 post deleted");
            db.close();
            res.type('html').status(200);
            res.end();
          });

        }
   });
});
});

 app.use('/editPost', (req, res) => {
        console.log("LOOKING FOR Post to dlete?");
        var dbo = db.db("myDatabase");
        var collection = dbo.collection("users");

    // construct the query object
    var myquery = {_id : req.body.id};
   	var text = {text : req.body.text};
        // if there's a name in the query parameter, use it here
         var returnArray = [];
    //queryObject = { "post" : req.query.id };
    console.log(queryObject);
    Post.find( queryObject, (err, posts) => {
        if (err) {
            console.log('uh oh' + err);
            res.json([]);
        }
        else if (posts.length == 0) {
            // no objects found, so send back empty json


            res.json([]);
        }
        else if (posts.length == 1 ) {
            var post = posts[0];
                dbo.collection("posts").updateOne(myquery, function(err, obj) {
            if (err) throw res.type('html').status(405);
            console.log("1 post deleted");
            db.close();
            res.type('html').status(200);
            res.write(removePost);
            res.end();
          });

        }
   });

});

//image stuf

/*************************************************/
// notifications here:

		var twilio = require('twilio');
		var querystring = require('querystring');

		// These are Zach's credentials
		var client = new twilio('AC2ce59a473a9e7499a3cbbcfa2e46032f', '55bd614d88a271f266594a350afe0a96');

		app.use('/sendtext', (req, res) => {
			// Send the text message.

			var m = {
			  to: '+1'.concat(req.query.number),
			  from: '+18509405408',
			  body: req.query.message
			};

			console.log(req.query);

			//client.messages.create(m);

			res.json(m);

		});


		app.use('/login', (req, res) => {
			res.redirect('/public/loginForm.html')
		});

		app.use('/admincheck', (req, res) => {

			var good = 0;

			var search = User.find({'username' : req.body.username});
			search.limit(1);
			search.exec(function (err, users) {
				if (users.length > 0) {
					if (users[0].admin == 1) {
						console.log(users[0].admin);
						res.redirect('/');
						res.end();
					} else{
						res.redirect('/public/badLogin.html');
					}
				}
			});
		});




	app.use('/getComment', (req, res) => {
		console.log("LOOKING FOR Post?");
	  console.log(req.query.id);

	// construct the query object
	var query;
	    // if there's a name in the query parameter, use it here
	query = query.id;

	Post.find( queryObject, (err, posts) => {
		if (err) {
		    console.log('uh oh' + err);
		    res.json({});
		}
		else if (posts.length == 0) {
		    // no objects found, so send back empty json
		    res.json({});
		}
		else if (posts.length == 1 ) {
			if(posts.id == id){
				var post = posts[0];
		    // send back a single JSON object
		    res.json(post.comments);
			}


		}
		else {
		    // construct an array out of the result
		    var returnArray = [];
		    posts.forEach( (post) => {
		    	if(post.user == req.query.id)
			   	 returnArray.push(post.comments);
			});
		    // send it back as JSON Array
		    res.json(returnArray);
		}

	    });
});

/*************************************************/
app.use('/public', express.static('public'));
app.use('/all', (req, res) => { res.redirect('/public/allUsers.html'); } );

app.use('/deleteusergui', (req, res) => { res.redirect('/public/deleteuser.html'); } );
app.use('/editusergui', (req, res) => { res.redirect('/public/editUser.html'); } );
app.use('/createadmingui', (req, res) => { res.redirect('/public/createAdmin.html'); } );
app.use('/createusergui', (req, res) => { res.redirect('/public/createUser.html'); } );
app.use('/allPosts', (req, res) => { res.redirect('/public/allPosts.html'); } );
app.use('/remove', (req, res) => { res.redirect('/public/removePost.html'); } );
app.use('/', (req, res) => { res.redirect('/public/dashboard.html');});
app.use('/login', (req, res) => { res.redirect('/public/loginForm.html');});
app.listen(3000,  () => {
	console.log('Listening on port 3000');
    });
