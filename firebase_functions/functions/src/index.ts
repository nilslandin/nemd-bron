import * as functions from 'firebase-functions';
const admin = require('firebase-admin');
admin.initializeApp();

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
export const helloWorld = functions.https.onRequest((request, response) => {
//  const original = request.query.text;

 // response.send("Hello from Firebase!" + original);

    admin.firestore().collection('pendingRequeest').add({HpID: "testId", pnr: "1912121212"});

    return response.json({result: `Message with ID: added.`});
});
