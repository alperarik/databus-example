from kafka import KafkaConsumer

consumer = KafkaConsumer(
    'data_analysis_topic',
     bootstrap_servers=['kafka-1:9092'],
     enable_auto_commit=True,
     group_id='python-kafka-consumer',
     value_deserializer=lambda m: m.decode('utf-8'),
     key_deserializer=lambda m: m.decode('utf-8'))

print('Start listening')

for message in consumer:
    value = message.value
    key = message.key
    print('Message key: {} value: {} read'.format(key, value))