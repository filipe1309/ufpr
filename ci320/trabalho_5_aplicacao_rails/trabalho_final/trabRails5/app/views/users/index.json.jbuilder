json.array!(@users) do |user|
  json.extract! user, :id, :login, :password, :admin
  json.url user_url(user, format: :json)
end
