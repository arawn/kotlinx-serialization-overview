const serialization = require("./build/distributions/kotlinx-serialization.js")

const movies = serialization.Movies
const movie = movies.fromJson('{"title":"foo","director":"x","rating":0.1}')

console.log(`Movie (title: ${movie.title}, director: ${movie.director}, rating: ${movie.rating})`);
