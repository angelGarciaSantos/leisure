(function () {
	angular.module('services').service('commentsService', commentsService);

	function commentsService($resource) {
		/**  Estamos devolviendo recursos, que internamente usan promesas */

		this.comments = $resource(
            "http://localhost:8080/leisure/comments/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

        this.commentsByEvent = $resource(
            "http://localhost:8080/leisure/comments/event/:id/:first/:max", // plantilla de la url del api
			{ id: '@id', first:'@first', max:'@max'  }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.commentEvent = $resource(
            "http://localhost:8080/leisure/private/comments/:eventId/:userId", // plantilla de la url del api
			{ eventId: '@eventId',userId: '@userId' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 


	};
} ());