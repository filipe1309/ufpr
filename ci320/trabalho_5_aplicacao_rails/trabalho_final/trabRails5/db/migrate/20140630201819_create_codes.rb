class CreateCodes < ActiveRecord::Migration
  def change
    create_table :codes do |t|
      t.string :name
      t.integer :book_id

      t.timestamps
    end
  end
end
