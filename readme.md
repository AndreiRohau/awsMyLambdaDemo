NEW USAGE
create a code commit in aws according to this sources
then create a codebuild entity in aws
then set up pipeline to create an s3 bucket for saving files and sending notifications to 
created aws lambda which
calls sns sending a message to subscribers.
all this is set up as a cloudformation stack during codebuild phase.
see buildspec.yml and cloudformation script template.yaml









Package this and use jar as a lambda function

{
  "msg": "demo_success_msg",
  "awsAccessKeyId": "_awsAccessKeyId_",
  "awsSecretKey": "_awsSecretKey_",
  "awsRegion": "_xx-xxxx-0_",
  "awsSnsTopicArn": "_arn:aws:sns:xx-xxxx-0:000000000000:MyOwnSnsTopic",
  "awsSqsUrl": "_https://sqs.xx-xxxx-0.amazonaws.com/000000000000/MyOwnQueue_",
  "awsSqsQueueName": "_MyOwnQueue_"
}