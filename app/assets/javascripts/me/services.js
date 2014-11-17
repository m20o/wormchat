(function () {
    'use strict';

    var roomModule = angular.module('RoomService', []);

    roomModule.factory('Room', ['$http', function ($http) {
            return {
                list: function () {
                    return $http.get('/api/room');
                }
            };

        }]
    );
}());