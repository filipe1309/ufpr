json.array!(@books_stores) do |books_store|
  json.extract! books_store, :id, :book_id, :store_id
  json.url books_store_url(books_store, format: :json)
end
