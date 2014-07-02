json.array!(@editions) do |edition|
  json.extract! edition, :id, :title, :book_id, :number
  json.url edition_url(edition, format: :json)
end
