(function () {
	angular.module('services').service('artistsService', artistsService);

	function artistsService($resource) {
		/**  Estamos devolviendo recursos, que internamente usan promesas */

		this.artists = $resource(
            "http://localhost:8080/leisure/artists/:id/:first/:max", // plantilla de la url del api
			{ id: '@id',first:'@first',max:'@max' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.artistsP = $resource(
            "http://localhost:8080/leisure/admin/artists/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
		
		this.artistsByKeywords = $resource(
            "http://localhost:8080/leisure/artists/keywords/:keywords/:first/:max", // plantilla de la url del api
			{ keywords: '@keywords',first:'@first',max:'@max' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
		//this.total = $resource("/api/priv/movimientos/totales/");
		
		this.artistsByEvent = $resource(
            "http://localhost:8080/leisure/artists/event/:id/:first/:max", // plantilla de la url del api
			{ id: '@id',first:'@first',max:'@max'}, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		);

		this.recommendedArtists = $resource(
            "http://localhost:8080/leisure/private/artists/user/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		);

		this.artistsByUser = $resource(
            "http://localhost:8080/leisure/artists/user/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		);

		this.followArtist = $resource(
            "http://localhost:8080/leisure/private/artist/user/:artistId/:userId", // plantilla de la url del api
			{ artistId: '@artistId', userId:'@userId' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
	};
} ());