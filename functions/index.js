const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.addPendingRequestAndNotifyPatient = functions.https.onRequest((request, response) => {

 	return admin.firestore().collection("pendingRequests").add({
		ssn: request.query.ssn, 
		hpid: request.query.hpid,
		requestApproved: false,
		requestRejected: false
	}).then((result) => {
		return	admin.firestore().collection("users").where('ssn', '==', request.query.ssn).get().then(snapshot => {
			if (snapshot.empty) {
	 			return response.send("No matching documents");
			}

			var user;
		    snapshot.forEach(doc => {
		    	user = doc;
			});

	//    	var hpname = await admin.firestore().collection

		    const payload = {
				notification: {
			        title: "Allow access to your medical records?",
			        body: "Health care provider " + request.query.hpid + " wants access to your medical records. Approve or reject?",
		    	},
				data: {
		      		requestId: result.id
				}
		    };
		    // Send notifications to all tokens.
		    admin.messaging().sendToDevice(user.data().firebase_token, payload);
			return response.json({result: "Got user from Firebase!" + user.data().firebase_token});
		})
	})
 });

exports.registerRequestApprovedRejected = functions.https.onRequest((request, response) => {
	return admin.firestore().collection("pendingRequests").document(request.query.requestId).get().then(document => 
	{
		if (request.query.accepted === true) {
				return response.json({result: "Request approved" + document.data().requestApproved});
		//document.requestApproved = true;
		}
		else {
				return response.json({result: "Request declined" + document.data().requestApproved});
		}
		//admin.firestore.collection("pendingRequests").document(request.query.requestId).set()
	})
	

 });