(function () {
	angular.module('services').service('localsService', localsService);

	function localsService($resource) {
		/**  Estamos devolviendo recursos, que internamente usan promesas */

		this.locals = $resource(
            "http://localhost:8080/leisure/locals/:id/:first/:max", // plantilla de la url del api
			{ id: '@id',first:'@first',max:'@max'  }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.localsP = $resource(
            "http://localhost:8080/leisure/admin/locals/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
		
		this.localsByKeywords = $resource(
            "http://localhost:8080/leisure/locals/keywords/:keywords/:first/:max", // plantilla de la url del api
			{ keywords: '@keywords' ,first:'@first',max:'@max' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
	};
} ());