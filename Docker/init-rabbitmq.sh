#!/bin/bash

echo "Creating user springuser..."
rabbitmqctl add_user springuser springpassword
rabbitmqctl set_user_tags springuser administrator
rabbitmqctl set_permissions -p / springuser ".*" ".*" ".*"