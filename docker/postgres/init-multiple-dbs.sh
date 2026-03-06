#!/bin/sh
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
  SELECT 'CREATE DATABASE ecommerce_product'
  WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ecommerce_product')\gexec

  SELECT 'CREATE DATABASE ecommerce_order'
  WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ecommerce_order')\gexec
EOSQL

