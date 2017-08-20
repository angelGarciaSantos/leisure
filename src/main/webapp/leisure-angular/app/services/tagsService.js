(function () {
	angular.module('services').service('tagsService', tagsService);

	function tagsService($resource) {
		/**  Estamos devolviendo recursos, que internamente usan promesas */

		this.tags = $resource(
            "http://localhost:8080/leisure/tags/:id/:first/:max", // plantilla de la url del api
			{ id: '@id',first:'@first',max:'@max'  }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.tagsP = $resource(
            "http://localhost:8080/leisure/admin/tags/:id", // plantilla de la url del api
			{ id: '@id' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
		
		this.tagsByKeywords = $resource(
            "http://localhost:8080/leisure/tags/keywords/:keywords/:first/:max", // plantilla de la url del api
			{ keywords: '@keywords',first:'@first',max:'@max' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
		//this.total = $resource("/api/priv/movimientos/totales/");
		
		this.tagsByArtist = $resource(
            "http://localhost:8080/leisure/tags/artist/:id/:first/:max", // plantilla de la url del api
			{ id: '@id' ,first:'@first',max:'@max'}, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.addTagToArtist = $resource(
            "http://localhost:8080/leisure/admin/tag/artist/:tagId/:artistId", // plantilla de la url del api
			{ tagId: '@tagId', artistId: '@artistId' }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
	};
} ());