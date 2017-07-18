(function () {
	angular.module('services').service('eventsService', eventsService);

	function eventsService($resource) {
		/**  Estamos devolviendo recursos, que internamente usan promesas */

		this.events = $resource(
            "http://localhost:8080/leisure/events/:id/:first/:max", // plantilla de la url del api
			{ id: '@id',first:'@first',max:'@max' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.eventsP = $resource(
            "http://localhost:8080/leisure/admin/events/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.eventsByKeywords = $resource(
            "http://localhost:8080/leisure/events/keywords/:keywords/:first/:max", // plantilla de la url del api
			{ keywords: '@keywords', first:'@first',max:'@max' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.recommendedEvents = $resource(
            "http://localhost:8080/leisure/private/events/user/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		);

		this.eventsByUser = $resource(
            "http://localhost:8080/leisure/events/user/:id/:first/:max", // plantilla de la url del api
			{ id: '@id', first: '@first', max: '@max' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		);

		this.eventsByArtist = $resource(
            "http://localhost:8080/leisure/events/artist/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		);

		this.eventsByLocal = $resource(
            "http://localhost:8080/leisure/events/local/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		);

		this.eventsByTag = $resource(
            "http://localhost:8080/leisure/events/tag/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		);

		this.createEvent = $resource(
            "http://localhost:8080/leisure/admin/events/:localId", // plantilla de la url del api
			{ localId: '@localId' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.addArtistToEvent = $resource(
            "http://localhost:8080/leisure/admin/events/artist/:eventId/:artistId", // plantilla de la url del api
			{ eventId: '@eventId', artistId: '@artistId' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
		this.modifyLocalFromEvent = $resource(
            "http://localhost:8080/leisure/admin/events/local/:eventId/:localId", // plantilla de la url del api
			{ eventId: '@eventId', localId: '@localId' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.followEvent = $resource(
            "http://localhost:8080/leisure/private/event/user/:eventId/:userId", // plantilla de la url del api
			{ eventId: '@eventId', userId:'@userId' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
	};
} ());