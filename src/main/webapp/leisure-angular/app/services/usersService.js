(function () {
	angular.module('services').service('usersService', usersService);

	function usersService($resource) {
		/**  Estamos devolviendo recursos, que internamente usan promesas */

		this.users = $resource(
            "http://localhost:8080/leisure/admin/users/:id/:first/:max", // plantilla de la url del api
			{ id: '@id', first:'@first',max:'@max'  }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.usersByKeywords = $resource(
            "http://localhost:8080/leisure/admin/users/keywords/:keywords/:first/:max", // plantilla de la url del api
			{ keywords: '@keywords', first:'@first',max:'@max'  }, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.login = $resource(
            "http://localhost:8080/leisure/login", // plantilla de la url del api
			{}, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.logout = $resource(
            "http://localhost:8080/leisure/logout", // plantilla de la url del api
			{}, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.loginInfo = $resource(
            "http://localhost:8080/leisure/logininfo", // plantilla de la url del api
			{}, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 

		this.register = $resource(
            "http://localhost:8080/leisure/register", // plantilla de la url del api
			{}, // la plantilla se rellena con la propiedad id
			{ 'update': { method: 'PUT' } }// un método custom con el verobo put para actualizaciones
		); 
	};
} ());