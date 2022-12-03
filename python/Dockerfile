FROM python:rc-alpine3.14
COPY kafka.py /kafkarun.py
RUN pip install kafka-python
CMD [ "python3", "-u", "/kafkarun.py" ]