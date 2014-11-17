(function () {
    'use strict';

    var roomModule = angular.module('RoomModule', []);
    roomModule.controller('RoomController', function ($scope) {
        $scope.rooms = [
            {id: 1, name: "The duel at Bespin"}
        ];
    });


    var messageBoardModule = angular.module("MessageBoardModule", []);
    messageBoardModule.controller('MessageBoardController', function ($scope) {
        $scope.messages = [
            {who: "other", user: "Darth Vader", time: "2BY", text: "Join me Luke and I'll unleash the true power of the Dark Side"},
            {who: "me", user: "Luke Skywalker", time: "2BY", text: "No, never!"}
        ]
    });
}());
