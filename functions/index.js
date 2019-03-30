const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.addPendingRequest = functions.https.onRequest((request, response) => {

 	return admin.firestore().collection("pendingRequests").add({
		ssn: request.query.ssn, 
		hpid: request.query.hpid
	}).then((result) => {
	 	return response.send("Hello from Firebase!" + result.id + " end");
		// body...
	})
 });