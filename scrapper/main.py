from models.Product import Product
from requestlib.StandardRequester import Requester
from requestlib.CustomRequester import CustomRequester

from scrappers.categories.CategoryMenuScrapper import CategoryMenuScrapper
from serializers.json_serializers.JSONSerializer import JSONSerializer

import pika
import json 
import time

requester = Requester()
catalogue_response = requester.get_html("https://alcomarket.md/ro/catalog")
category_menu_scrapper = CategoryMenuScrapper(
    catalogue_response, "div", "mini-catalog__wrapper", requests_module=requester)
category_scrappers = category_menu_scrapper.get_category_scrappers()

print("Proceeding on scrapping category pages")

vodka = category_scrappers[4]
print(vodka)

products = vodka.get_products()
vodka_category = vodka.get_model()

print(vodka_category.get_products()[0])

rabbitmq_host = "localhost"
queue_name = "auction_items_queue"
routing_key = "auction_items"
exchange_name = "auction_items_exchange"

connection = pika.BlockingConnection(pika.ConnectionParameters(host=rabbitmq_host))
channel = connection.channel()

channel.exchange_declare(exchange=exchange_name, exchange_type='direct')
channel.queue_declare(queue=queue_name, durable=True)
channel.queue_bind(exchange=exchange_name, queue=queue_name, routing_key=routing_key)

for product in products:
    product_name = product.name
    product_price = product.price
    product_dict = {"name": product_name, "startingPrice": product_price}
    product_json = json.dumps(product_dict)
    channel.basic_publish(exchange=exchange_name, routing_key=routing_key, body=product_json)
    print(f"Sent {product_json}")
    
connection.close()
