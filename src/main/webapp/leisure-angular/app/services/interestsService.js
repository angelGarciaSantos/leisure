(function () {
	angular.module('services').service('interestsService', interestsService);

	function interestsService($resource) {
		/**  Estamos devolviendo recursos, que internamente usan promesas */

		this.interests = $resource(
            "http://localhost:8080/leisure/private/interests/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
		
		this.interestsByKeywords = $resource(
            "http://localhost:8080/leisure/private/interests/keywords/:keywords", // plantilla de la url del api
			{ keywords: '@keywords' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
		//this.total = $resource("/api/priv/movimientos/totales/");
		
		this.interestsByUser = $resource(
            "http://localhost:8080/leisure/private/interests/user/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 


		this.interestsByEvent = $resource(
            "http://localhost:8080/leisure/private/interests/event/:eventId/:userId", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 



	};
} ());