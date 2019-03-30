const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
// // Create and Deploy Your First Cloud Functions
export const helloWorld = functions.https.onRequest((request, response) => {
//  const original = request.query.text;

 // response.send("Hello from Firebase!" + original);

    admin.firestore().collection('pendingRequest').add({hpid: request.query.hpid,
    	ssn: request.query.ssn, accepted: false, declined: false, acceptedhps: []})
    .then(function (result) {
    	console.log("Document written with ID: %s", result.id);
    })

    return response.json({result: `Message with ID: added.`});
});
export const helloWorld2 = functions.https.onRequest((request, response) => {

	admin.firestore().collection('pendingRequest').where('ssn',"==", request.query.ssn).get();


    return response.json({result: `Message with ID: added.`});
});