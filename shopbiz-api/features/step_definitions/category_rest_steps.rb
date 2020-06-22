require 'json'
require 'httmultiparty'

Given(/^there are Category entries$/) do

end

When(/^an API client requests all Category entries$/) do
  @response = HTTMultiParty.get($endpoint + "/categories")
end

Then(/^(\d+) category entries are returned$/) do |size|
  expect(@response.code).to eq(200)
  data = JSON.parse(@response.body)
  #puts data
  expect(data.count ).to eq(size.to_i)
end