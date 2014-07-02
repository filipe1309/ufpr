json.array!(@codes) do |code|
  json.extract! code, :id, :name, :book_id
  json.url code_url(code, format: :json)
end
