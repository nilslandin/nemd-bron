const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.addPendingRequestAndNotifyPatient = functions.https.onRequest((request, response) => {

 	return admin.firestore().collection("pendingRequests").add({
		ssn: request.query.ssn, 
		hpid: request.query.hpid
	}).then((result) => {
		return	admin.firestore().collection("users").where('ssn', '==', request.query.ssn).get().then(snapshot => {
			if (snapshot.empty) {
	 			return response.send("No matching documents");
			}

			var user;
		    snapshot.forEach(doc => {
		    	user = doc;
			});

			return response.send("Got user from Firebase!" + user.data().firebase_token);
		})
	})
 });

